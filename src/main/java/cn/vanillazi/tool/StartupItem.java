package cn.vanillazi.tool;

import java.util.List;

public class StartupItem {

    private String name;
    private String executable;
    private List<String> args;
    private String description;
    private Boolean autoStart;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExecutable() {
        return executable;
    }

    public void setExecutable(String executable) {
        this.executable = executable;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }
}
