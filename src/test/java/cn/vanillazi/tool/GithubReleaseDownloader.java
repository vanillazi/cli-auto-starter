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
