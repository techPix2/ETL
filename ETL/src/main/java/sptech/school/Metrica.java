package sptech.school;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metrica {
    @JsonProperty("cpuPercent")
    private Double cpuPercent;
    @JsonProperty("cpuFreq")
    private Double cpuFreq;
    @JsonProperty("ramPercent")
    private Double ramPercent;
    @JsonProperty("ramUsed")
    private Double ramUsed;
    @JsonProperty("diskPercent")
    private Double diskPercent;
    @JsonProperty("diskUsed")
    private Double diskUsage;
    @JsonProperty("dateTime")
    private Date dateTime;

    public Double getCpuPercent() {
        return cpuPercent;
    }

    public void setCpuPercent(Double cpuPercent) {
        this.cpuPercent = cpuPercent;
    }

    public Double getCpuFreq() {
        return cpuFreq;
    }

    public void setCpuFreq(Double cpuFreq) {
        this.cpuFreq = cpuFreq;
    }

    public Double getRamPercent() {
        return ramPercent;
    }

    public void setRamPercent(Double ramPercent) {
        this.ramPercent = ramPercent;
    }

    public Double getRamUsed() {
        return ramUsed;
    }

    public void setRamUsed(Double ramUsed) {
        this.ramUsed = ramUsed;
    }

    public Double getDiskPercent() {
        return diskPercent;
    }

    public void setDiskPercent(Double diskPercent) {
        this.diskPercent = diskPercent;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
