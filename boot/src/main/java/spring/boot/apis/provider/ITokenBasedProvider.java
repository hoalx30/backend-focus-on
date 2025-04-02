package spring.boot.apis.provider;

public interface ITokenBasedProvider<T, U> {
    String buildScope(U user);

    String sign(U user, long expiredTime, String secretKey, String id, String referId);

    T verify(String token, String secretKey);
}
