package com.treetoplodge.treetoplodge_api.model;

import java.util.List;
import lombok.Data;

@Data
public class DeleteResult {
    private List<String> successful;
    private List<String> failed;
    
    public DeleteResult(List<String> successful, List<String> failed) {
        this.successful = successful;
        this.failed = failed;
    }
}
