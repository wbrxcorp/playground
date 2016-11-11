<?php
if (count(get_included_files()) == 1) define ('__main__', __FILE__);

require_once("global.php");

function curl_get_json_example() {
  $curl = curl_init();
  curl_setopt($curl, CURLOPT_URL, "https://jsonplaceholder.typicode.com/todos");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  curl_setopt($curl, CURLOPT_HTTPHEADER, array("X-Hoge-Foo: Bar", "X-Moge-Muga: Baz"));
  curl_setopt($curl, CURLOPT_HEADER, true); // include http response headers
  // curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false); // to skip veryfing SSL certificate
  $response = curl_exec($curl); // returns falsy value if fail, but returns something even when http status code is not successful value
  $status = curl_getinfo($curl); // http://php.net/manual/ja/function.curl-getinfo.php
  curl_close($curl);

  $http_code = $status["http_code"];
  $header_size = $status["header_size"];
  $headers = substr($response, 0, $header_size);
  // do something with response headers

  $body = substr($response, $header_size);

  return json_decode($body, true);
}

function curl_post_json_example() {
  $curl = curl_init();
  curl_setopt($curl, CURLOPT_URL, "https://jsonplaceholder.typicode.com/posts");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
  curl_setopt($curl, CURLOPT_HTTPHEADER, array("X-Hoge-Foo: Bar", "Content-Type: application/json"));
  curl_setopt($curl, CURLOPT_POSTFIELDS, json_encode(array("Foo"=>"Bar")));

  $response = curl_exec($curl); // returns falsy value if fail, but returns something even when http status code is not successful value
  $status = curl_getinfo($curl); // http://php.net/manual/ja/function.curl-getinfo.php
  var_dump($status);

  return json_decode($response, true);
}

function curl_get_xml_example() {
  $curl = curl_init();
  curl_setopt($curl, CURLOPT_URL, "http://www.thomas-bayer.com/sqlrest/CUSTOMER/");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  $response = curl_exec($curl);
  curl_close($curl);
  return simplexml_load_string($response);
}

function curl_post_xml_example() {
  $xml = new SimpleXMLElement("<?xml version=\"1.0\" encoding=\"utf-8\" ?><xml/>");
  $elem1 = $xml->addChild("anelement");
  $elem1[0] = "要素のテ<>キ&スト1";
  $elem1->addAttribute("attr1", "hoge");

  $xml->addChild("anelement", "要素のテキスト2"/*AIN'T BE AUTOMATICALLY ESCAPED!*/)->addAttribute("attr2", "ふ&<>が"/*AUTOMATICALLY ESCAPED*/);

  echo $xml->asXML();

  $curl = curl_init();
  curl_setopt($curl, CURLOPT_URL, "http://www.thomas-bayer.com/sqlrest/CUSTOMER/");
  curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
  curl_setopt($curl, CURLOPT_CUSTOMREQUEST, "POST");
  curl_setopt($curl, CURLOPT_HTTPHEADER, array("X-Hoge-Foo: Bar", "Content-Type: application/xml"));
  curl_setopt($curl, CURLOPT_POSTFIELDS, $xml->asXML());

  $response = curl_exec($curl); // returns falsy value if fail, but returns something even when http status code is not successful value
  $status = curl_getinfo($curl); // http://php.net/manual/ja/function.curl-getinfo.php
  var_dump($status);

  try {
    return simplexml_load_string($response); // may cause an error if xml is invalid
  }
  catch (ErrorException $e) {
    echo "exception caught";
    var_dump($e);
    return null;
  }
}

///////////////////////////////////////////////////////////////////////////
// run test when executed from cli directly
if (php_sapi_name() == "cli" && defined("__main__") && __main__ == __FILE__) {
  var_dump(curl_get_json_example());
  var_dump(curl_get_xml_example());
  var_dump(curl_post_json_example());
  var_dump(curl_post_xml_example());
}
?>
