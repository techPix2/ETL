package org.example;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Metrica {
    private Double cpuPercent;
    private Double cpuFreq;
    private Double ramPercent;
    private Double ramUsed;
    private Double sendPackages;
    private Double receivePackages;
    private Date dateTime;

    private Double diskPercent;
    private Double diskUsage;

    private Map<String, DiskInfo> discos = new HashMap<>();

    public static class DiskInfo {
        private final Double used;
        private final Double percent;

        public DiskInfo(Double used, Double percent) {
            this.used = used;
            this.percent = percent;
        }

        public Double getUsed() { return used; }
        public Double getPercent() { return percent; }
    }

    public Double getCpuPercent() { return cpuPercent; }
    public void setCpuPercent(Double cpuPercent) { this.cpuPercent = cpuPercent; }

    public Double getCpuFreq() { return cpuFreq; }
    public void setCpuFreq(Double cpuFreq) { this.cpuFreq = cpuFreq; }

    public Double getRamPercent() { return ramPercent; }
    public void setRamPercent(Double ramPercent) { this.ramPercent = ramPercent; }

    public Double getRamUsed() { return ramUsed; }
    public void setRamUsed(Double ramUsed) { this.ramUsed = ramUsed; }

    public Double getSendPackages() { return sendPackages; }
    public void setSendPackages(Double sendPackages) { this.sendPackages = sendPackages; }

    public Double getReceivePackages() { return receivePackages; }
    public void setReceivePackages(Double receivePackages) { this.receivePackages = receivePackages; }

    public Date getDateTime() { return dateTime; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    public Double getDiskPercent() { return diskPercent; }
    public void setDiskPercent(Double diskPercent) { this.diskPercent = diskPercent; }

    public Double getDiskUsage() { return diskUsage; }
    public void setDiskUsage(Double diskUsage) { this.diskUsage = diskUsage; }

    public void addDiskInfo(String disco, Double used, Double percent) {
        discos.put(disco.toUpperCase(), new DiskInfo(used, percent));
        if (disco.equalsIgnoreCase("C")) {
            this.diskUsage = used;
            this.diskPercent = percent;
        }
    }

    public Map<String, DiskInfo> getDiscos() {
        return discos;
    }
}
