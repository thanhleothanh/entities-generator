package org.example;

import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.FileUtils;
import org.example.schema.ConnectionManager;

public abstract class AbstractGeneratorContext extends AbstractMojo {
	@Parameter(defaultValue = "${project}")
	private MavenProject project;
	@Parameter(property = "jdbcUrl", required = true)
	private String jdbcUrl;
	@Parameter(property = "jdbcUser", required = true)
	private String jdbcUser;
	@Parameter(property = "jdbcPassword", required = true)
	private String jdbcPassword;
	@Parameter(property = "basePackageName", required = true)
	private String basePackageName;
	@Parameter(property = "outputDirectory", defaultValue = "${project.build.directory}/generated-sources/src")
	protected File outputDirectory;
	@Parameter(property = "removeOldOutput", defaultValue = "false")
	private boolean removeOldOutput;
	@Parameter(property = "skip", defaultValue = "false")
	private boolean skip;

	public static String packageName;
	public static Log log;
	public static ConnectionManager connectionManager;

	@Override
	public void execute() throws MojoExecutionException {
		beforeExecute();
		initContext();
		doExecute();
	}

	private void beforeExecute() throws MojoExecutionException {
		if (skip) {
			getLog().info("Skipping entities generator!");
			return;
		}
		try {
			FileUtils.forceMkdir(outputDirectory);
		} catch (final IOException ioe) {
			throw new MojoExecutionException("Failed to create directory: " + outputDirectory, ioe);
		}
		if (removeOldOutput) {
			try {
				FileUtils.cleanDirectory(outputDirectory);
			} catch (final IOException ioe) {
				throw new MojoExecutionException("Failed to clean directory: " + outputDirectory, ioe);
			}
		}
	}

	private void initContext() {
		project.addCompileSourceRoot(outputDirectory.getPath());
		packageName = basePackageName;
		log = getLog();
		connectionManager = new ConnectionManager(jdbcUrl, jdbcUser, jdbcPassword);
	}

	public abstract void doExecute();

}
