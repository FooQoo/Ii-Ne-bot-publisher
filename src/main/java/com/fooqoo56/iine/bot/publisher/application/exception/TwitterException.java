package com.fooqoo56.iine.bot.publisher.application.exception;

public abstract class TwitterException extends RuntimeException {

    private static final long serialVersionUID = -8870701197622795497L;

    /**
     * デフォルトコンストラクタ.
     */
    public TwitterException() {

    }

    /**
     * message引数コンストラクタ.
     *
     * @param message メッセージ
     */
    public TwitterException(final String message) {
        super(message);
    }

    /**
     * コンストラクタ.
     *
     * @param message メッセージ
     * @param cause   Throwable
     */
    public TwitterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
