
package org.example.model;

public class ResultOperation {
    private String result;
    private String reason;

    public ResultOperation(String result, String reason) {
        this.result = result;
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public String getReason() {
        return reason;
    }
}
