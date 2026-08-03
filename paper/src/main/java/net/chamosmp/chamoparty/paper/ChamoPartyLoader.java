package net.chamosmp.chamoparty.paper;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

@SuppressWarnings("UnstableApiUsage")
public class ChamoPartyLoader implements PluginLoader {

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver mavenCentral = new MavenLibraryResolver();
        mavenCentral.addRepository(new RemoteRepository.Builder("central", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR).build());
        mavenCentral.addDependency(new Dependency(new DefaultArtifact("com.zaxxer:HikariCP:4.0.3"), null));
        mavenCentral.addDependency(new Dependency(new DefaultArtifact("redis.clients:jedis:7.2.0"), null));
        mavenCentral.addDependency(new Dependency(new DefaultArtifact("com.mysql:mysql-connector-j:8.3.0"), null));

        classpathBuilder.addLibrary(mavenCentral);
    }
}
