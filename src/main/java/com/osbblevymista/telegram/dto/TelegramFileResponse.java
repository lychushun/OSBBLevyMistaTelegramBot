package com.osbblevymista.telegram.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TelegramFileResponse {

    @JsonProperty("ok")
    public String ok;

    @JsonProperty(namespace="result")
    public String file_id;

    @JsonProperty(namespace="result")
    public String file_unique_id;

    @JsonProperty(namespace="result")
    public String file_size;

    @JsonProperty(namespace="result")
    public String file_path;

}
