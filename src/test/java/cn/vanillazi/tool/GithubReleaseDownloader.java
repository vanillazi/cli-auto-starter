package cn.vanillazi.tool;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

public class GithubReleaseDownloader {

    private static GitHub github;

    @BeforeAll
    public static void setup() throws IOException {
        github= GitHub.connectUsingOAuth("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");

    }

    @Test
    public void test() throws IOException {
        var repo=github.getRepository("go-gost/gost");
        var releases=repo.listReleases();
        releases.forEach(release->{
            System.out.println(release.getName());
            try {
                release.listAssets().forEach(a->{
                    System.out.println(a.getName()+"-->"+a.getBrowserDownloadUrl());
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Test
    public void getLastRelease() throws IOException {
        var repo=github.getRepository("go-gost/gost");
        var releases=repo.listReleases();
        releases.toList().stream().limit(1).forEach(release->{
            System.out.println(release.getName());
            try {
                release.listAssets().forEach(a->{
                    System.out.println(a.getName()+"-->"+a.getBrowserDownloadUrl());
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
