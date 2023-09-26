package cn.vanillazi.tool;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;

public class StartupItem {

    private String name;
    private String executable;
    private String workDirectory;
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

    public String getWorkDirectory() {
        return workDirectory;
    }

    public void setWorkDirectory(String workDirectory) {
        this.workDirectory = workDirectory;
    }

    public String digest(){

        MessageDigest md= null;
        try {
            md = MessageDigest.getInstance("SHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        var sb=new StringBuilder();
        sb.append(executable);
        if(args!=null){
            var argString=args.stream().collect(Collectors.joining(","));
            sb.append(argString);
        }
        if(workDirectory!=null){
            sb.append(workDirectory);
        }
        var digest=md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(digest);
    }

    public boolean isModified(StartupItem startupItem){
        return !this.digest().equals(startupItem.digest());
    }

}
