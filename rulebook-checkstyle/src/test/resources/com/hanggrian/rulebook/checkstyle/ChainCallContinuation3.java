package com.hanggrian.rulebook.checkstyle;

public class ChainCallContinuation {
    public void foo() {
        new StringBuilder(
            "Lorem ipsum"
        ).append(0).append(2);
    }

    public void bar() {
        new Baz().baz()
            .baz();
    }

    public static class Baz {
        public Baz baz() {
            return this;
        }
    }
}
