package com.api.serviceCounter.shared.apiHealth;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class VersionController {

    private final Optional<BuildProperties> build;
    private final Optional<GitProperties> git;

    public VersionController(Optional<BuildProperties> build, Optional<GitProperties> git) {
        this.build = build;
        this.git = git;
    }

    @GetMapping("/version")
    public Map<String, Object> version() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("name", build.map(BuildProperties::getName).orElse("serviceCounter"));
        out.put("version", build.map(BuildProperties::getVersion).orElse("dev"));
        out.put("time", build.map(b -> b.getTime().toString()).orElse(null));
        out.put("commit", git.map(GitProperties::getCommitId).orElse(null));
        out.put("branch", git.map(GitProperties::getBranch).orElse(null));
        return out;
    }
}