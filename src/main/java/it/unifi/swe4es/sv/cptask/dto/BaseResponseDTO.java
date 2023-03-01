package it.unifi.swe4es.sv.cptask.dto;

public class BaseResponseDTO {
    private String result;

    public String getResult() {
        return result;
    }

    public BaseResponseDTO(String result) {
        this.result = result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
