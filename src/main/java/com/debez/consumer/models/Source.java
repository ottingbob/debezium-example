
package com.debez.consumer.models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "version",
    "connector",
    "name",
    "ts_ms",
    "snapshot",
    "db",
    "table",
    "server_id",
    "gtid",
    "file",
    "pos",
    "row",
    "thread",
    "query"
})
public class Source {

    @JsonProperty("version")
    private String version;
    @JsonProperty("connector")
    private String connector;
    @JsonProperty("name")
    private String name;
    @JsonProperty("ts_ms")
    private Long tsMs;
    @JsonProperty("snapshot")
    private String snapshot;
    @JsonProperty("db")
    private String db;
    @JsonProperty("table")
    private String table;
    @JsonProperty("server_id")
    private Integer serverId;
    @JsonProperty("gtid")
    private Object gtid;
    @JsonProperty("file")
    private String file;
    @JsonProperty("pos")
    private Integer pos;
    @JsonProperty("row")
    private Integer row;
    @JsonProperty("thread")
    private Integer thread;
    @JsonProperty("query")
    private Object query;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    public Source withVersion(String version) {
        this.version = version;
        return this;
    }

    @JsonProperty("connector")
    public String getConnector() {
        return connector;
    }

    @JsonProperty("connector")
    public void setConnector(String connector) {
        this.connector = connector;
    }

    public Source withConnector(String connector) {
        this.connector = connector;
        return this;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Source withName(String name) {
        this.name = name;
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

    public Source withTsMs(Long tsMs) {
        this.tsMs = tsMs;
        return this;
    }

    @JsonProperty("snapshot")
    public String getSnapshot() {
        return snapshot;
    }

    @JsonProperty("snapshot")
    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public Source withSnapshot(String snapshot) {
        this.snapshot = snapshot;
        return this;
    }

    @JsonProperty("db")
    public String getDb() {
        return db;
    }

    @JsonProperty("db")
    public void setDb(String db) {
        this.db = db;
    }

    public Source withDb(String db) {
        this.db = db;
        return this;
    }

    @JsonProperty("table")
    public String getTable() {
        return table;
    }

    @JsonProperty("table")
    public void setTable(String table) {
        this.table = table;
    }

    public Source withTable(String table) {
        this.table = table;
        return this;
    }

    @JsonProperty("server_id")
    public Integer getServerId() {
        return serverId;
    }

    @JsonProperty("server_id")
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Source withServerId(Integer serverId) {
        this.serverId = serverId;
        return this;
    }

    @JsonProperty("gtid")
    public Object getGtid() {
        return gtid;
    }

    @JsonProperty("gtid")
    public void setGtid(Object gtid) {
        this.gtid = gtid;
    }

    public Source withGtid(Object gtid) {
        this.gtid = gtid;
        return this;
    }

    @JsonProperty("file")
    public String getFile() {
        return file;
    }

    @JsonProperty("file")
    public void setFile(String file) {
        this.file = file;
    }

    public Source withFile(String file) {
        this.file = file;
        return this;
    }

    @JsonProperty("pos")
    public Integer getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public Source withPos(Integer pos) {
        this.pos = pos;
        return this;
    }

    @JsonProperty("row")
    public Integer getRow() {
        return row;
    }

    @JsonProperty("row")
    public void setRow(Integer row) {
        this.row = row;
    }

    public Source withRow(Integer row) {
        this.row = row;
        return this;
    }

    @JsonProperty("thread")
    public Integer getThread() {
        return thread;
    }

    @JsonProperty("thread")
    public void setThread(Integer thread) {
        this.thread = thread;
    }

    public Source withThread(Integer thread) {
        this.thread = thread;
        return this;
    }

    @JsonProperty("query")
    public Object getQuery() {
        return query;
    }

    @JsonProperty("query")
    public void setQuery(Object query) {
        this.query = query;
    }

    public Source withQuery(Object query) {
        this.query = query;
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

    public Source withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("version", version).append("connector", connector).append("name", name).append("tsMs", tsMs).append("snapshot", snapshot).append("db", db).append("table", table).append("serverId", serverId).append("gtid", gtid).append("file", file).append("pos", pos).append("row", row).append("thread", thread).append("query", query).append("additionalProperties", additionalProperties).toString();
    }

}
