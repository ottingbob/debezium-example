
package com.debez.consumer.models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "before",
    "after",
    "source",
    "op",
    "ts_ms"
})
public class Payload {

    @JsonProperty("before")
    private Before before;
    @JsonProperty("after")
    private After after;
    @JsonProperty("source")
    private Source source;
    @JsonProperty("op")
    private String op;
    @JsonProperty("ts_ms")
    private Long tsMs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("before")
    public Before getBefore() {
        return before;
    }

    @JsonProperty("before")
    public void setBefore(Before before) {
        this.before = before;
    }

    public Payload withBefore(Before before) {
        this.before = before;
        return this;
    }

    @JsonProperty("after")
    public After getAfter() {
        return after;
    }

    @JsonProperty("after")
    public void setAfter(After after) {
        this.after = after;
    }

    public Payload withAfter(After after) {
        this.after = after;
        return this;
    }

    @JsonProperty("source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(Source source) {
        this.source = source;
    }

    public Payload withSource(Source source) {
        this.source = source;
        return this;
    }

    @JsonProperty("op")
    public String getOp() {
        return op;
    }

    @JsonProperty("op")
    public void setOp(String op) {
        this.op = op;
    }

    public Payload withOp(String op) {
        this.op = op;
        return this;
    }

    @JsonProperty("ts_ms")
    public Long getTsMs() {
        return tsMs;
    }

    @JsonProperty("ts_ms")
    public void setTsMs(Long tsMs) {
        this.tsMs = tsMs;
    }

    public Payload withTsMs(Long tsMs) {
        this.tsMs = tsMs;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Payload withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Payload.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("before");
        sb.append('=');
        sb.append(((this.before == null)?"<null>":this.before));
        sb.append(',');
        sb.append("after");
        sb.append('=');
        sb.append(((this.after == null)?"<null>":this.after));
        sb.append(',');
        sb.append("source");
        sb.append('=');
        sb.append(((this.source == null)?"<null>":this.source));
        sb.append(',');
        sb.append("op");
        sb.append('=');
        sb.append(((this.op == null)?"<null>":this.op));
        sb.append(',');
        sb.append("tsMs");
        sb.append('=');
        sb.append(((this.tsMs == null)?"<null>":this.tsMs));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.op == null)? 0 :this.op.hashCode()));
        result = ((result* 31)+((this.tsMs == null)? 0 :this.tsMs.hashCode()));
        result = ((result* 31)+((this.before == null)? 0 :this.before.hashCode()));
        result = ((result* 31)+((this.after == null)? 0 :this.after.hashCode()));
        result = ((result* 31)+((this.source == null)? 0 :this.source.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Payload) == false) {
            return false;
        }
        Payload rhs = ((Payload) other);
        return (((((((this.op == rhs.op)||((this.op!= null)&&this.op.equals(rhs.op)))&&((this.tsMs == rhs.tsMs)||((this.tsMs!= null)&&this.tsMs.equals(rhs.tsMs))))&&((this.before == rhs.before)||((this.before!= null)&&this.before.equals(rhs.before))))&&((this.after == rhs.after)||((this.after!= null)&&this.after.equals(rhs.after))))&&((this.source == rhs.source)||((this.source!= null)&&this.source.equals(rhs.source))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))));
    }

}
