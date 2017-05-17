import java.nio.charset.Charset


def PROJECT_NAME = "operation"

// デフォルトのログレベル
def DEFAULT_LOG_LEVEL = WARN

// デフォルトのログレベル
def APPLICATION_LOG_LEVEL = INFO

// ログファイルの出力先ディレクトリ
def LOGFILE_HOME = "/tmp"

// ログの出力フォーマット
def LOG_FORMAT = "%d{yyyy/MM/dd HH:mm:ss.SSS} [%level] [%thread] [%logger] %message%n"

// ログのデフォルト文字コード
def DEFAULT_CHARSET = Charset.forName("UTF-8")

// ローカルだけ設定を変更
if (System.getProperty("os.name").startsWith("Mac")) {
    APPLICATION_LOG_LEVEL = TRACE
    LOGFILE_HOME = "build/tomcat/logs"
}

// ログファイルへのログ出力設定
appender("FILE", RollingFileAppender) {

    file = "${LOGFILE_HOME}/${PROJECT_NAME}/logs/application.log"

    encoder(PatternLayoutEncoder) {
        charset = DEFAULT_CHARSET
        pattern = LOG_FORMAT
    }

    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${LOGFILE_HOME}/${PROJECT_NAME}/logs/application.log.%d{yyyy-MM-dd}"
    }
}

// 標準出力へのログ出力設定
appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = DEFAULT_CHARSET
        pattern = LOG_FORMAT
    }
}

// ログレベル設定
root(DEFAULT_LOG_LEVEL, ["FILE", "STDOUT"])

logger("jp.co.biglobe.isp", APPLICATION_LOG_LEVEL)
