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
        mavenCentral.addDependency(new Dependency(new DefaultArtifact("io.lettuce:lettuce-core:7.0.0.RELEASE"), null));

        classpathBuilder.addLibrary(mavenCentral);
    }
}