<?php
// コンフィグの読み込み
define("CONFIG_PHP", dirname(__FILE__) . "/config.php");
define("CONFIG_DEFAULT_PHP", dirname(__FILE__) . "/config_default.php");
if (file_exists(CONFIG_PHP)) { require_once(CONFIG_PHP); }
else if (file_exists(CONFIG_DEFAULT_PHP)) {require_once(CONFIG_DEFAULT_PHP);}

///////////////////////////////////////////////////////////////////////////
// HTTPのエラーステータスを例外で表現するためのクラス
class HttpErrorStatus extends Exception {}

///////////////////////////////////////////////////////////////////////////
// 例外発生時の処理
function exception_handler($exception) {
    // HTTPエラーステータスの場合
    if ($exception instanceof HttpErrorStatus) {
        $message = sprintf("%d %s", $exception->getCode(), $exception->getMessage());
        if (php_sapi_name() != "cli") header(sprintf("HTTP/1.1 %s", $message));
        echo $message;
        exit;
    }
    // その他の例外の場合は500エラーを返す TODO: XHRの場合はJSONでレスポンスする
    debug_log($exception);
    if (php_sapi_name() != "cli") header("HTTP/1.1 500 Internal Server Error");
    echo "<html><body><h1>500 Internal Server Error</h1>";
    echo $exception->getMessage();
    echo "<pre>";
    echo $exception->getTraceAsString();
    echo "</pre>";
    echo "</body></html>";
    exit;
}

set_exception_handler("exception_handler");

///////////////////////////////////////////////////////////////////////////
// (REST APIでは特に)エラーに対しては容赦なく例外を上げてHTTPレベルでエラーにすべき
function exception_error_handler($errno, $errstr, $errfile, $errline ) {
    throw new ErrorException($errstr, $errno, 0, $errfile, $errline);
}

set_error_handler("exception_error_handler");

///////////////////////////////////////////////////////////////////////////
// デバッグログ出力用関数
function debug_log($value) {
  // PHPの組み込みWebサーバで実行されている時以外はデバッグログを出力しない
  // (cliの時はテストと限らず、バッチ処理の場合もあるので)
  if (php_sapi_name() != "cli-server") return;

  $value_type = gettype($value);
  if ($value_type == "array" || $value_type == "object") {
    $value = var_export($value, TRUE);
  }

  error_log($value);
}

///////////////////////////////////////////////////////////////////////////
// このスクリプト自身のURLを生成する
function script_url() {
  $scheme = (isset($_SERVER["HTTPS"]) && $_SERVER["HTTPS"])? "https":"http";
  return sprintf("%s://%s%s/", $scheme, $_SERVER["HTTP_HOST"], $_SERVER["SCRIPT_NAME"]);
}

///////////////////////////////////////////////////////////////////////////
// 古いPHPのために boolval関数を提供
if (PHP_VERSION_ID < 50500) {
  function boolval($var) {
    return !!$var;
  }
}

///////////////////////////////////////////////////////////////////////////
// 文字列をNFKC正規化または mb_convert_kanaにかける
function normalize($str) {
  if ($str === null) return null;
  if (method_exists("Normalizer", "normalize")) { // intlモジュール有効時(PHP5.3以降)
    debug_log("Using Normalizer");
    return Normalizer::normalize($str, Normalizer::FORM_KC);
  }
  // else
  if (function_exists("mb_convert_kana")) { // intlがなければmbstringを使用
    // 「全角」英数字を「半角」に変換
    // 「全角」スペースを「半角」に変換
    // 「半角カタカナ」を「全角カタカナ」に変換
    // 濁点付きの文字を一文字に変換
    debug_log("Using mb_convert_kana");
    return mb_convert_kana($str, "asKV", "UTF-8");
  }
  // intlもmbstringもなければあきらめる
  return $str;
}

///////////////////////////////////////////////////////////////////////////
// 文字列をiso-2022-jpに互換できる形に変換する（要するに機種依存文字を変換）
function to_iso2022jp_compatible_string($str){
  if ($str === null) return null;
  $arr=array(
    "\xE2\x85\xA0" => "I", "\xE2\x85\xA1" => "II", "\xE2\x85\xA2" => "III", "\xE2\x85\xA3" => "IV",	"\xE2\x85\xA4" => "V",
    "\xE2\x85\xA5" => "VI",	"\xE2\x85\xA6" => "VII","\xE2\x85\xA7" => "VIII",	"\xE2\x85\xA8" => "IX",	"\xE2\x85\xA9" => "X",
    "\xE2\x85\xB0" => "i",	"\xE2\x85\xB1" => "ii",	"\xE2\x85\xB2" => "iii",	"\xE2\x85\xB3" => "iv",	"\xE2\x85\xB4" => "v",
    "\xE2\x85\xB5" => "vi",	"\xE2\x85\xB6" => "vii",	"\xE2\x85\xB7" => "viii",	"\xE2\x85\xB8" => "ix", "\xE2\x85\xB9" => "x",
    "\xE2\x91\xA0" => "(1)", "\xE2\x91\xA1" => "(2)",	"\xE2\x91\xA2" => "(3)", "\xE2\x91\xA3" => "(4)",	"\xE2\x91\xA4" => "(5)",
    "\xE2\x91\xA5" => "(6)",	"\xE2\x91\xA6" => "(7)",	"\xE2\x91\xA7" => "(8)",	"\xE2\x91\xA8" => "(9)",	"\xE2\x91\xA9" => "(10)",
    "\xE2\x91\xAA" => "(11)",	"\xE2\x91\xAB" => "(12)",	"\xE2\x91\xAC" => "(13)",	"\xE2\x91\xAD" => "(14)",	"\xE2\x91\xAE" => "(15)",
    "\xE2\x91\xAF" => "(16)",	"\xE2\x91\xB0" => "(17)",	"\xE2\x91\xB1" => "(18)",	"\xE2\x91\xB2" => "(19)",	"\xE2\x91\xB3" => "(20)",
    "\xE3\x8A\xA4" => "(上)", "\xE3\x8A\xA5" => "(中)",	"\xE3\x8A\xA6" => "(下)", "\xE3\x8A\xA7" => "(左)", "\xE3\x8A\xA8" => "(右)",
    "\xE3\x8D\x89" => "ミリ",	"\xE3\x8D\x8D" => "メートル",	"\xE3\x8C\x94" => "キロ",	"\xE3\x8C\x98" => "グラム",
    "\xE3\x8C\xA7" => "トン",	"\xE3\x8C\xA6" => "ドル",	"\xE3\x8D\x91" => "リットル",
    "\xE3\x8C\xAB" => "パーセント", "\xE3\x8C\xA2" => "センチ",
    "\xE3\x8E\x9D" => "cm",	"\xE3\x8E\x8F" => "kg",	"\xE3\x8E\xA1" => "m2",	"\xE3\x8F\x8D" => "K.K.",
    "\xE2\x84\xA1" => "TEL",	"\xE2\x84\x96" => "No.",
    "\xE3\x8D\xBB" => "平成",	"\xE3\x8D\xBC" => "昭和",	"\xE3\x8D\xBD" => "大正",	"\xE3\x8D\xBE" => "明治",
    "\xE3\x88\xB1" => "(株)",	"\xE3\x88\xB2" => "(有)",	"\xE3\x88\xB9" => "(代)",
  );
  return str_replace(array_keys($arr),array_values($arr),$str);
}

///////////////////////////////////////////////////////////////////////////
// テスト
if (php_sapi_name() == "cli" && isset($argv[1]) && $argv[1] == "test") {
  error_log("normalize('ｱｲｳ１２３')=" . normalize("ｱｲｳ１２３"));
  error_log("to_iso2022jp_compatible_string('①②③')=" . to_iso2022jp_compatible_string("①②③"));
}
?>
