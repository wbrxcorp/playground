<?php
if (count(get_included_files()) == 1) define ('__main__', __FILE__);

require_once("global.php");
if (!defined("DBHOST")) define("DBHOST", "localhost");
if (!defined("DBNAME")) define("DBNAME", "playground");
if (!defined("DBUSER")) define("DBUSER", "playground");
if (!defined("DBPASSWORD")) define("DBPASSWORD", "");

///////////////////////////////////////////////////////////////////////////
// データベース接続を得る
function get_database_connection() {
  return new PDO("mysql:dbname=" . DBNAME . ";charset=utf8",DBUSER,DBPASSWORD,array(PDO::ATTR_ERRMODE=>PDO::ERRMODE_EXCEPTION));
}

///////////////////////////////////////////////////////////////////////////
// examples
function database_insert_example($db, $deptId, $name, $sal, $birthDate, $available) {
  $stmt = $db->prepare("insert into emp(dept_id, name, sal, birth_date, available, created_at) values(:deptId, :name,:sal,:birthDate,:available,now())");
  $stmt->bindValue(":deptId", $deptId, PDO::PARAM_INT);
  $stmt->bindValue(":name", $name, PDO::PARAM_STR);
  $stmt->bindValue(":sal", $sal, PDO::PARAM_INT);
  $stmt->bindValue(":birthDate", $birthDate, PDO::PARAM_STR);
  $stmt->bindValue(":available", $available, PDO::PARAM_BOOL);
  $stmt->execute();
  return $db->lastInsertId();
}

function database_select_example($db) {
  $stmt = $db->prepare("select id,dept_id,name,sal,birth_date,available,created_at,updated_at from emp where available=true");
  $stmt->execute();
  $emps = array();
  while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
    $emps []= array(
      "id"=>intval($row[0]),
      "dept_id"=>intval($row[1]),
      "name"=>$row[2],
      "sal"=>intval($row[3]),
      "birthDate"=>$row[4],
      "available"=>boolval($row[5]),
      "createdAt"=>DateTime::createFromFormat("Y-m-d H:i:s", $row[6]),
      "updatedAt"=>$row[6]? DateTime::createFromFormat("Y-m-d H:i:s", $row[7]) : null
    );
  }
  return $emps;
}

function database_select_one_to_many_example($db) {
  $stmt = $db->prepare("select dept.id,dept.name,emp.id,emp.name,sal,birth_date,available,created_at,updated_at from dept,emp where dept.id=dept_id and available=true");
  $stmt->execute();
  $depts = array();
  while ($row = $stmt->fetch(PDO::FETCH_NUM)) {
    $deptId = intval($row[0]);
    if (!isset($depts[$deptId])) $depts[$deptId] = array("id"=>$deptId,"name"=>$row[1],"emps"=>array());
    $depts[$deptId]["emps"] []= array(
      "id"=>intval($row[2]),
      "dept_id"=>$deptId,
      "name"=>$row[3],
      "sal"=>intval($row[4]),
      "birthDate"=>$row[5],
      "available"=>boolval($row[6]),
      "createdAt"=>DateTime::createFromFormat("Y-m-d H:i:s", $row[7]),
      "updatedAt"=>$row[6]? DateTime::createFromFormat("Y-m-d H:i:s", $row[8]) : null
    );
  }
  return $depts;
}

function database_update_example($db, $empId, $name, $sal, $birthDate, $available) {
  $stmt = $db->prepare("update emp set name=:name,sal=:sal,birth_date=:birthDate,available=:available,updated_at=now() where id=:empId");
  $stmt->bindValue(":name", $name, PDO::PARAM_STR);
  $stmt->bindValue(":sal", $sal, PDO::PARAM_INT);
  $stmt->bindValue(":birthDate", $birthDate, PDO::PARAM_STR);
  $stmt->bindValue(":available", $available, PDO::PARAM_BOOL);
  $stmt->bindValue(":empId", $empId, PDO::PARAM_INT);
  return $stmt->execute(); // returns boolean
}

function database_delete_example($db, $empId) {
  $stmt = $db->prepare("delete from emp where id=:empId");
  $stmt->bindValue(":empId", $empId, PDO::PARAM_INT);
  return $stmt->execute(); // returns boolean
}

///////////////////////////////////////////////////////////////////////////
// run test when executed from cli directly
if (php_sapi_name() == "cli" && defined("__main__") && __main__ == __FILE__) {
  $db = get_database_connection();

  echo "create table\n";
  $db->query("create temporary table dept(id serial primary key, name varchar(64))");
  $db->query("create temporary table emp(id serial primary key, dept_id int not null, name varchar(64) not null, sal int not null, birth_date date, available bool not null,created_at datetime not null, updated_at datetime)");

  echo "begin transaction\n";
  $db->beginTransaction();
  try {
    echo "insert dept1\n";
    $db->query("insert into dept(name) values('営業部')");
    $dept1 = $db->lastInsertId();
    echo "insert dept2\n";
    $db->query("insert into dept(name) values('総務部')");
    $dept2 = $db->lastInsertId();

    echo "insert emp1\n";
    $emp1 = database_insert_example($db, $dept1, "しまりそ", 1000000, null, true);
    echo "insert emp2\n";
    $emp2 = database_insert_example($db, $dept1, "そりまし", 1100000, null, true);
    echo "insert emp3\n";
    $emp3 = database_insert_example($db, $dept2, "そまりし", 1200000, "2001-09-10", true);

    echo "update emp2\n";
    database_update_example($db, $emp2, "そりまし", 1001000, "1976-09-15", true);

    echo "select emp table\n";
    $emps = database_select_example($db);
    printf("id\tdept_id\tname\tsal\tbirth_date\tavailable\tcreated_at\tupdated_at\n");
    foreach ($emps as $emp) {
      printf("%d\t%d\t%s\t%d\t%s\t%s\t%s\t%s\n",
        $emp["id"], $emp["dept_id"], $emp["name"], $emp["sal"], $emp["birthDate"],
        $emp["available"]? "true" : "false",
        $emp["createdAt"]->format("Y-m-d H:i:s"), $emp["updatedAt"]? $emp["updatedAt"]->format("Y-m-d H:i:s") : "NULL");
    }

    echo "select dept and emp tables at once\n";
    $depts = database_select_one_to_many_example($db);
    foreach ($depts as $dept) {
      printf("dept id=%d name=%s\n", $dept["id"], $dept["name"]);
      printf("id\tname\n");
      foreach ($dept["emps"] as $emp) {
        printf("%d\t%s\n", $emp["id"], $emp["name"]);
      }
    }

    echo "delete emp1\n";
    database_delete_example($db, $emp1);

    echo "commit transaction\n";
    $db->commit();
  }
  catch (Exception $e) {
    echo "rollback transaction\n";
    $db->rollback();
    throw $e;
  }
}
?>
