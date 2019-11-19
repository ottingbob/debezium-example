
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
    private Long serverId;
    @JsonProperty("gtid")
    private Object gtid;
    @JsonProperty("file")
    private String file;
    @JsonProperty("pos")
    private Long pos;
    @JsonProperty("row")
    private Long row;
    @JsonProperty("thread")
    private Long thread;
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
    public Long getServerId() {
        return serverId;
    }

    @JsonProperty("server_id")
    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Source withServerId(Long serverId) {
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
    public Long getPos() {
        return pos;
    }

    @JsonProperty("pos")
    public void setPos(Long pos) {
        this.pos = pos;
    }

    public Source withPos(Long pos) {
        this.pos = pos;
        return this;
    }

    @JsonProperty("row")
    public Long getRow() {
        return row;
    }

    @JsonProperty("row")
    public void setRow(Long row) {
        this.row = row;
    }

    public Source withRow(Long row) {
        this.row = row;
        return this;
    }

    @JsonProperty("thread")
    public Long getThread() {
        return thread;
    }

    @JsonProperty("thread")
    public void setThread(Long thread) {
        this.thread = thread;
    }

    public Source withThread(Long thread) {
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
        StringBuilder sb = new StringBuilder();
        sb.append(Source.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("version");
        sb.append('=');
        sb.append(((this.version == null)?"<null>":this.version));
        sb.append(',');
        sb.append("connector");
        sb.append('=');
        sb.append(((this.connector == null)?"<null>":this.connector));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("tsMs");
        sb.append('=');
        sb.append(((this.tsMs == null)?"<null>":this.tsMs));
        sb.append(',');
        sb.append("snapshot");
        sb.append('=');
        sb.append(((this.snapshot == null)?"<null>":this.snapshot));
        sb.append(',');
        sb.append("db");
        sb.append('=');
        sb.append(((this.db == null)?"<null>":this.db));
        sb.append(',');
        sb.append("table");
        sb.append('=');
        sb.append(((this.table == null)?"<null>":this.table));
        sb.append(',');
        sb.append("serverId");
        sb.append('=');
        sb.append(((this.serverId == null)?"<null>":this.serverId));
        sb.append(',');
        sb.append("gtid");
        sb.append('=');
        sb.append(((this.gtid == null)?"<null>":this.gtid));
        sb.append(',');
        sb.append("file");
        sb.append('=');
        sb.append(((this.file == null)?"<null>":this.file));
        sb.append(',');
        sb.append("pos");
        sb.append('=');
        sb.append(((this.pos == null)?"<null>":this.pos));
        sb.append(',');
        sb.append("row");
        sb.append('=');
        sb.append(((this.row == null)?"<null>":this.row));
        sb.append(',');
        sb.append("thread");
        sb.append('=');
        sb.append(((this.thread == null)?"<null>":this.thread));
        sb.append(',');
        sb.append("query");
        sb.append('=');
        sb.append(((this.query == null)?"<null>":this.query));
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
        result = ((result* 31)+((this.query == null)? 0 :this.query.hashCode()));
        result = ((result* 31)+((this.thread == null)? 0 :this.thread.hashCode()));
        result = ((result* 31)+((this.version == null)? 0 :this.version.hashCode()));
        result = ((result* 31)+((this.serverId == null)? 0 :this.serverId.hashCode()));
        result = ((result* 31)+((this.tsMs == null)? 0 :this.tsMs.hashCode()));
        result = ((result* 31)+((this.file == null)? 0 :this.file.hashCode()));
        result = ((result* 31)+((this.connector == null)? 0 :this.connector.hashCode()));
        result = ((result* 31)+((this.pos == null)? 0 :this.pos.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.gtid == null)? 0 :this.gtid.hashCode()));
        result = ((result* 31)+((this.row == null)? 0 :this.row.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.snapshot == null)? 0 :this.snapshot.hashCode()));
        result = ((result* 31)+((this.db == null)? 0 :this.db.hashCode()));
        result = ((result* 31)+((this.table == null)? 0 :this.table.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Source) == false) {
            return false;
        }
        Source rhs = ((Source) other);
        return ((((((((((((((((this.query == rhs.query)||((this.query!= null)&&this.query.equals(rhs.query)))&&((this.thread == rhs.thread)||((this.thread!= null)&&this.thread.equals(rhs.thread))))&&((this.version == rhs.version)||((this.version!= null)&&this.version.equals(rhs.version))))&&((this.serverId == rhs.serverId)||((this.serverId!= null)&&this.serverId.equals(rhs.serverId))))&&((this.tsMs == rhs.tsMs)||((this.tsMs!= null)&&this.tsMs.equals(rhs.tsMs))))&&((this.file == rhs.file)||((this.file!= null)&&this.file.equals(rhs.file))))&&((this.connector == rhs.connector)||((this.connector!= null)&&this.connector.equals(rhs.connector))))&&((this.pos == rhs.pos)||((this.pos!= null)&&this.pos.equals(rhs.pos))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.gtid == rhs.gtid)||((this.gtid!= null)&&this.gtid.equals(rhs.gtid))))&&((this.row == rhs.row)||((this.row!= null)&&this.row.equals(rhs.row))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.snapshot == rhs.snapshot)||((this.snapshot!= null)&&this.snapshot.equals(rhs.snapshot))))&&((this.db == rhs.db)||((this.db!= null)&&this.db.equals(rhs.db))))&&((this.table == rhs.table)||((this.table!= null)&&this.table.equals(rhs.table))));
    }

}
