# Akka & Spring Boot サンプル

## これはなに？

* 昔々(2.0.x）の [Akkaチュートリアル](http://doc.akka.io/docs/akka/2.0/intro/getting-started-first-java.html)の
コードを最新(2.5.x)のAPIに移植したもの
* 円周率（3.14...）を複数のスレッドで分散計算するサンプル。円周率の計算ロジックはチュートリアルの説明を参考のこと
* 元々は、Pi.java ファイルに全てのクラスが記述されていたが、個人的な趣味でクラスごとにファイルを分離
* SpringBoot は起動に使ってるだけで、Actorの部分で Injection は使っていない。Actor に SpringFramework で Injection するにところは調査中

## 起動方法

SpringBoot のコマンドラインアプリです。gradleで実行可能

    ```
    % ./gradlew clean bootRun
    ```

## コードを読むときは、

src/main/java/jp/co/biglobe/sample/pi/service/PiService が起点

## TODO

そのうち、コンポーネント図てきなやつを用意する
