package cn.vanillazi.tool;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class StartupItem {

    private String name;
    private String executable;
    private List<String> args;
    private String description;
    private Boolean autoStart;
}
