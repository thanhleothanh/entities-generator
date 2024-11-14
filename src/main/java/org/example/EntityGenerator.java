package org.example;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "entities-generate")
public final class EntityGenerator extends AbstractMojo {

	@Parameter(property = "jdbcUrl", required = true)
	private String jdbcUrl;
	@Parameter(property = "jdbcUser", required = true)
	private String jdbcUser;
	@Parameter(property = "jdbcPassword", required = true)
	private String jdbcPassword;


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating entities!");
		getLog().info(jdbcUrl);
		getLog().info(jdbcUser);
		getLog().info(jdbcPassword);

	}
}
