package modules.instagram

// https://www.instagram.com/shimariso/media/

package object instagram {
  override protected implicit def jsonFormats: org.json4s.Formats = org.json4s.DefaultFormats

  def getTheLatestImages(username:String):Seq[Item] = {

  }
}

/*
{
   "items" : [
      {
         "images" : {
            "low_resolution" : {
               "height" : 320,
               "url" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s320x320/e35/15535408_1881802905384513_836738691018784768_n.jpg?ig_cache_key=MTQwNTY5Mjc5ODk0Njg5MTMxNQ%3D%3D.2",
               "width" : 320
            },
            "standard_resolution" : {
               "width" : 640,
               "height" : 640,
               "url" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s640x640/sh0.08/e35/15535408_1881802905384513_836738691018784768_n.jpg?ig_cache_key=MTQwNTY5Mjc5ODk0Njg5MTMxNQ%3D%3D.2"
            },
            "thumbnail" : {
               "width" : 150,
               "url" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s150x150/e35/15535408_1881802905384513_836738691018784768_n.jpg?ig_cache_key=MTQwNTY5Mjc5ODk0Njg5MTMxNQ%3D%3D.2",
               "height" : 150
            }
         },
         "can_view_comments" : true,
         "type" : "image",
         "id" : "1405692798946891315_4266532154",
         "created_time" : "1481791661",
         "user" : {
            "username" : "shimariso",
            "id" : "4266532154",
            "profile_picture" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-19/s150x150/15275690_357002748010247_338788562201739264_a.jpg",
            "full_name" : "Tomoatsu Shimada"
         },
         "alt_media_url" : null,
         "likes" : {
            "count" : 0,
            "data" : []
         },
         "caption" : {
            "from" : {
               "full_name" : "Tomoatsu Shimada",
               "profile_picture" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-19/s150x150/15275690_357002748010247_338788562201739264_a.jpg",
               "username" : "shimariso",
               "id" : "4266532154"
            },
            "id" : "17861397094071418",
            "created_time" : "1481791661",
            "text" : "海"
         },
         "can_delete_comments" : false,
         "link" : "https://www.instagram.com/p/BOCBicojdYz/",
         "location" : null,
         "code" : "BOCBicojdYz",
         "comments" : {
            "count" : 0,
            "data" : []
         }
      },
      {
         "can_view_comments" : true,
         "images" : {
            "thumbnail" : {
               "width" : 150,
               "url" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s150x150/e35/15535368_1830323517186238_7140911938858385408_n.jpg?ig_cache_key=MTQwNTY4Njk1NDMyNjc2MjI5MA%3D%3D.2",
               "height" : 150
            },
            "standard_resolution" : {
               "url" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s640x640/sh0.08/e35/15535368_1830323517186238_7140911938858385408_n.jpg?ig_cache_key=MTQwNTY4Njk1NDMyNjc2MjI5MA%3D%3D.2",
               "height" : 640,
               "width" : 640
            },
            "low_resolution" : {
               "height" : 320,
               "url" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s320x320/e35/15535368_1830323517186238_7140911938858385408_n.jpg?ig_cache_key=MTQwNTY4Njk1NDMyNjc2MjI5MA%3D%3D.2",
               "width" : 320
            }
         },
         "created_time" : "1481790964",
         "type" : "image",
         "id" : "1405686954326762290_4266532154",
         "user" : {
            "full_name" : "Tomoatsu Shimada",
            "profile_picture" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-19/s150x150/15275690_357002748010247_338788562201739264_a.jpg",
            "id" : "4266532154",
            "username" : "shimariso"
         },
         "alt_media_url" : null,
         "likes" : {
            "count" : 0,
            "data" : []
         },
         "link" : "https://www.instagram.com/p/BOCANZaDCMy/",
         "can_delete_comments" : false,
         "caption" : null,
         "code" : "BOCANZaDCMy",
         "location" : null,
         "comments" : {
            "data" : [],
            "count" : 0
         }
      },
      {
         "images" : {
            "low_resolution" : {
               "width" : 320,
               "height" : 320,
               "url" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s320x320/e35/15276703_1865721456989591_393821386914660352_n.jpg?ig_cache_key=MTQwNTU1MTk5ODY0NDU5MTY5MQ%3D%3D.2"
            },
            "standard_resolution" : {
               "width" : 640,
               "height" : 640,
               "url" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s640x640/sh0.08/e35/15276703_1865721456989591_393821386914660352_n.jpg?ig_cache_key=MTQwNTU1MTk5ODY0NDU5MTY5MQ%3D%3D.2"
            },
            "thumbnail" : {
               "width" : 150,
               "height" : 150,
               "url" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-15/s150x150/e35/15276703_1865721456989591_393821386914660352_n.jpg?ig_cache_key=MTQwNTU1MTk5ODY0NDU5MTY5MQ%3D%3D.2"
            }
         },
         "can_view_comments" : true,
         "type" : "image",
         "id" : "1405551998644591691_4266532154",
         "created_time" : "1481774876",
         "user" : {
            "id" : "4266532154",
            "username" : "shimariso",
            "full_name" : "Tomoatsu Shimada",
            "profile_picture" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-19/s150x150/15275690_357002748010247_338788562201739264_a.jpg"
         },
         "likes" : {
            "count" : 1,
            "data" : [
               {
                  "username" : "kashira2010",
                  "id" : "212177244",
                  "profile_picture" : "https://scontent-nrt1-1.cdninstagram.com/t51.2885-19/11371164_1023995207624126_1325841477_a.jpg",
                  "full_name" : ""
               }
            ]
         },
         "alt_media_url" : null,
         "can_delete_comments" : false,
         "caption" : null,
         "link" : "https://www.instagram.com/p/BOBhhiIjRxL/",
         "location" : null,
         "code" : "BOBhhiIjRxL",
         "comments" : {
            "data" : [],
            "count" : 0
         }
      }
   ],
   "more_available" : false,
   "status" : "ok"
}
*/
