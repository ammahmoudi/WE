package response;

public
class Response {
    ResponseType type;
    String data;

    public
    Response() {

    }
    public
    Response(ResponseType type, String data) {
        this.type = type;
        this.data = data;
    }

    public
    ResponseType getType() {
        return type;
    }

    public
    Response setType(ResponseType type) {
        this.type = type;
        return this;
    }

    public
    String getData() {
        return data;
    }

    public
    Response setData(String data) {
        this.data = data;
        return this;
    }
}
