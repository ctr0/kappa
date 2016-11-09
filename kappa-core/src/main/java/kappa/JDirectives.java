package kappa;

import java.util.function.Function;

/**
 * Created by j0rd1 on 6/11/16.
 */
public class JDirectives {

    /*
    public interface JavaDirective<R>
            extends Function<Directive<R>, Directive<R>> {
    }

    public interface JavaDirective1<T, R>
            extends Function<Function<T, Directive<R>>, Directive<R>> {
    }

    public static <R> JavaDirective<R> nextDirective() {
        return directive -> directive;
    }

    public static <T, R> JavaDirective1<T, R> nextDirective(T argument) {
        return directive -> directive.apply(argument);
    }

    public static class JavaDirectiveWrapper<R> {

        private Directive<R> directive;

        public Directive<R> success(Directive<R> f) {
            return directive = f;
        }

        public Directive<R> failure(Directive<R> f) {
            directive.failure = f;
            return directive;
        }
    }
    */

}
