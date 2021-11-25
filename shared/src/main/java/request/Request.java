package request;

public
class Request {
    Integer userId;
    RequestType type;
    String data;
    String token;

    public
    RequestType getType() {
        return type;
    }

    public
    Request setType(RequestType type) {
        this.type = type;
        return this;
    }

    public
    String getData() {
        return data;
    }

    public
    Request setData(String data) {
        this.data = data;
        return this;
    }

    public
    String getToken() {
        return token;
    }

    public
    Request setToken(String token) {
        this.token = token;
        return this;
    }

    public
    Request(RequestType type, String data) {
        this.type = type;
        this.data = data;

    }

    public
    Request(Integer userId, RequestType type, String data, String token) {
        this.userId = userId;
        this.type = type;
        this.data = data;
        this.token = token;
    }

    public
    Request() {

    }

    public
    Integer getUserId() {
        return userId;
    }

    public
    Request setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }
}
