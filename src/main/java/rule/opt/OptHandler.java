package rule.opt;

public interface OptHandler<T> {
    String  handler(String v1, String v2, String opt);

    T finalResult(String v );

}
