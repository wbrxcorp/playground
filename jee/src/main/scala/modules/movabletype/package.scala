package modules

import _root_.scalikejdbc.{DB,DBSession}

package object movabletype extends _root_.scalikejdbc.SQLInterpolation {
  def getMovableTypeAuthor(name:String):Option[Author] = {
    DB readOnly { implicit session =>
      sql"select author_id,author_name,author_nickname from mt_author where author_name=${name}".map { row =>
        Author(id=row.int(1),name=row.string(2),nickname=row.stringOpt(3))
      }.single.apply
    }
  }
  def getMovableTypeAuthor(name:String, password:String):Author = {
    // TODO: crypt以外の2タイプの暗号化方式にも対応すること MT/Auth/MT.pm is_valid_password()
    DB readOnly { implicit session =>
      sql"select author_id,author_name,author_nickname,author_password from mt_author where author_name=${name}".map { row =>
        val (id, name, nickname, encryptedPassword) = (row.int(1),row.string(2),row.stringOpt(3),row.string(4))
        if (org.apache.commons.codec.digest.Crypt.crypt(password,encryptedPassword.substring(0,2)) == encryptedPassword) {
          Some(Author(id, name, nickname))
        } else None
      }.single.apply.flatten.getOrElse(throw new SecurityException("Authentication Failed: %s".format(name)))
    }
  }

  //def getMovableTypeEntriesWithoutCategory(blogId:Int)(implicit session:DBSession):Seq[Entry] = {
  //}

  def getMovableTypeEntriesByCategoryId(categoryId:Int)(implicit session:DBSession):Seq[Entry] = {
    sql"select entry_id,entry_blog_id,entry_category_id,entry_class,entry_keywords,entry_status,entry_template_id,entry_text,entry_text_more,entry_title,entry_unpublished_on from mt_entry where exists(select * from mt_placement where placement_entry_id=entry_id and placement_category_id=${categoryId})".map { row =>
      Entry(id=row.int(1), blogId=row.int(2), categoryId=row.intOpt(3), entryClass=row.stringOpt(4), keywords=row.stringOpt(5),
        status=row.int(6), templateId=row.intOpt(7), text=row.stringOpt(8), textMore=row.stringOpt(9), title=row.stringOpt(10),
        unpublishedOn=row.jodaLocalDateTimeOpt(11))
    }.list.apply
  }

  def getMovableTypeEntryCustomFieldValues(entryId:Int)(implicit session:DBSession):Map[String,(String,Any)] = {
    sql"select field_basename,field_name,field_type,entry_meta_vchar,entry_meta_vchar_idx,entry_meta_vdatetime,entry_meta_vdatetime_idx,entry_meta_vinteger,entry_meta_vinteger_idx,entry_meta_vfloat,entry_meta_vblob,entry_meta_vclob from mt_entry_meta,mt_field,mt_entry where field_obj_type='entry' and concat('field.',field_basename) = entry_meta_type and entry_meta_entry_id=entry_id and field_blog_id=entry_blog_id and entry_id=${entryId}".map { row =>
      val (fieldBaseName, fieldName, fieldType) = (row.string(1), row.string(2), row.string(3))
      val value = fieldType match {
        case "checkbox" => row.intOpt(9).getOrElse(0)
        case "image" => row.stringOpt(12).getOrElse("")
        case "select" => row.stringOpt(5).getOrElse("")
        case "text" => row.stringOpt(5).getOrElse("")
        case "textarea" => row.stringOpt(12).getOrElse("")
      }
      // vchar, vdatetime, vintegfer, vfloat, vblob, vclob) =
      (fieldBaseName, (fieldName, value))
    }.list.apply.toMap
  }

  def getMovableTypeCategoryCustomFieldValues(categoryId:Int)(implicit session:DBSession):Map[String,(String,Any)] = {
    sql"select field_basename,field_name,field_type,category_meta_vchar,category_meta_vchar_idx,category_meta_vdatetime,category_meta_vdatetime_idx,category_meta_vinteger,category_meta_vinteger_idx,category_meta_vfloat,category_meta_vblob,category_meta_vclob from mt_category_meta,mt_field,mt_category where field_obj_type='category' and concat('field.',field_basename) = category_meta_type and category_meta_category_id=category_id and field_blog_id=category_blog_id and category_id=${categoryId}".map { row =>
      val (fieldBaseName, fieldName, fieldType) = (row.string(1), row.string(2), row.string(3))
      val value = fieldType match {
        case "checkbox" => row.intOpt(9).getOrElse(0)
        case "image" => row.stringOpt(12).getOrElse("")
        case "select" => row.stringOpt(5).getOrElse("")
        case "text" => row.stringOpt(5).getOrElse("")
        case "textarea" => row.stringOpt(12).getOrElse("")
      }
      // vchar, vdatetime, vintegfer, vfloat, vblob, vclob) =
      (fieldBaseName, (fieldName, value))
    }.list.apply.toMap
  }

  def getMovableTypeCategories(parentId:Int)(implicit session:DBSession):Seq[Category] = {
    sql"select category_id,category_blog_id,category_label,category_class,category_parent from mt_category where category_parent=${parentId}".map { row =>
      val categoryId = row.int(1)
      Category(id=categoryId, blogId=row.int(2), label=row.string(3), categoryClass=row.string(4), parent=row.intOpt(5).filter(_ != 0),
        children=getMovableTypeCategories(categoryId))
    }.list.apply
  }

  def getMovableTypeCategoriesByBlogId(blogId:Int)(implicit session:DBSession):Seq[Category] = {
    sql"select category_id,category_blog_id,category_label,category_class,category_parent from mt_category where category_blog_id=${blogId} and (category_parent=0 or category_parent is null)".map { row =>
      val categoryId = row.int(1)
      Category(id=row.int(1), blogId=row.int(2), label=row.string(3), categoryClass=row.string(4), parent=row.intOpt(5).filter(_ != 0),
        children=getMovableTypeCategories(categoryId))
    }.list.apply
  }

  def getMovableTypeCategory(id:Int)(implicit session:DBSession):Option[Category] = {
    sql"select category_id,category_blog_id,category_label,category_class,category_parent from mt_category where category_id=${id}".map { row =>
      val categoryId = row.int(1)
      Category(id=row.int(1), blogId=row.int(2), label=row.string(3), categoryClass=row.string(4), parent=row.intOpt(5).filter(_ != 0),
        children=getMovableTypeCategories(categoryId))
    }.single.apply
  }

  def getMovableTypeBlogs(parentId:Int)(implicit session:DBSession):Seq[Blog] = {
    sql"select blog_id,blog_name,blog_class,blog_parent_id from mt_blog where blog_parent_id=${parentId}".map { row =>
      Blog(id=row.int(1), name=row.string(2), blogClass=row.string(3))
    }.list.apply
  }

  def getMovableTypeBlog(id:Int)(implicit session:DBSession):Option[Blog] = {
    sql"select blog_id,blog_name,blog_class,blog_parent_id from mt_blog where blog_id=${id}".map { row =>
      val (id, name, blogClass, parent) = (row.int(1), row.string(2), row.string(3), row.intOpt(4))
      Blog(id, name, blogClass, parent.map(getMovableTypeBlog(_)).flatten, getMovableTypeBlogs(id))
    }.single.apply
  }

  def getMovableTypeBlog(name:String)(implicit session:DBSession):Option[Blog] = {
    sql"select blog_id,blog_name,blog_class,blog_parent_id from mt_blog where blog_name=${name}".map { row =>
      val (id, name, blogClass, parent) = (row.int(1), row.string(2), row.string(3), row.intOpt(4))
      Blog(id, name, blogClass, parent.map(getMovableTypeBlog(_)).flatten, getMovableTypeBlogs(id))
    }.single.apply
  }

  // getMovableTypeBlog("求人情報 - 業界業種別 -")
  // select entry_id,entry_title,entry_status,entry_unpublished_on from mt_entry where entry_blog_id=3
  // select field_id,field_obj_type,field_basename,field_name,field_type from mt_field where field_blog_id=3
  // select entry_meta_entry_id,entry_meta_type,entry_meta_vclob from mt_entry_meta,mt_entry where entry_id=entry_meta_entry_id and entry_blog_id=3 and entry_meta_type='field.entrydatajobsupply';
  // select category_meta_category_id,category_meta_type,category_meta_vclob from mt_category_meta;
}
