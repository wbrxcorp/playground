# 分け方

profile

module


プロファイルによって切り替えたいもの
webapp/WEB-INF/web.xml
ScalatraBootstrap.scala
htdocs

JNDI pathはプロジェクト名で切り替わってる

playgroundから他プロジェクトを派生させるアイディアがもともと駄目では → 再利用したい部分: Main, build.sbt, その他各モジュール

再利用の方法
- jar にして maven ← 陳腐化とソースの可視性が問題
- ソースをupstream ← マージしんどい
- 検索しやすいようにしてコピペ ← 理想。SO方式。陳腐化対応がしんどいかもしれないが必要な所だけやればいいので悪くはない？

playgroundをwarにできる必要はあるのか - 難しい。war運用は捨てるべき？でもサンプルくらいは残したい


playgroundの方針案1: .md書いて検索しやすいようにすることを前提に、再利用はコピペ。
- プロファイル分けはH2かMySQLか（あと送信メールサーバ）を分けられるくらい。 flywayのmigrationフォルダは分けないとだめ、migrationは各モジュールでやる? profile分けまで出来るようにするとコンフィギュレーション増えすぎ

- プロダクション環境でscala console使ってデータいじりたいねん(executable jarとても魅力的) ←でもそれ他に選択肢ないかな？

warは捨てる。プロダクションはPHPで決め打ち。
- コマンドラインはどうする？ やはり scala console は捨てがたい。となると jarも捨てがたい(でも捨てるか？playgroundに限って言えばjarは必要ないかも)
- package objectの import

cmsやwbportの処遇。playgroundの一部だととても開発しやすいが、cmsはともかくとしてwbportはplaygroundのサブディレクトリにしちゃってええんかいな→ええんちゃう？

seleniumでテストできるようにしたい ということは war運用可能なスタイルに据え置かないとだめでは？←要確認

ScalatraBootstrapでだけDB初期化するのも手か

sbtコンソールはあきらめてH2コンソールにせよ？

scalaの記述性と方安全に身を委ねる。scala consoleではscalikejdbcだけ実行できればよいではないか？

sbt run したら console.readlineは効かない、server.stopを呼ぶ機会がない

プロファイルの切換をsbtに依存するのはだめ
