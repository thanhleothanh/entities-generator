package org.example.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.example.AbstractGeneratorContext;
import org.example.model.entity.Entity;
import org.example.service.TemplateService;

public class FtlTemplateService implements TemplateService {

	@Override
	public void process(List<Entity> entities, File outputDirectory, boolean overwriteExistingFiles) {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
		cfg.setLocale(Locale.US);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setClassForTemplateLoading(AbstractGeneratorContext.class, "/template");

		try {
			Template template = cfg.getTemplate("_entity.ftl");
			for (Entity entity : entities) {
				AbstractGeneratorContext.log.info(String.format("\tProcessing entity (%s)", entity.getName()));

				Path outputPath = Path.of(outputDirectory.getPath(), "%s.java".formatted(entity.getName()));
				OpenOption[] fileOptions = overwriteExistingFiles ?
						new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING} :
						new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW};

				try (Writer fileWriter = Files.newBufferedWriter(outputPath, fileOptions)) {
					template.process(Map.of("entity", entity), fileWriter);
				} catch (FileAlreadyExistsException e) {
					AbstractGeneratorContext.log.info(String.format("\tIgnoring entity (%s). If you wish to replace existing files, set overwriteExistingFiles=true. If you wish to delete all existing files, set cleanOutputDirectory=true.", entity.getName()));
				} catch (TemplateException | IOException e) {
					AbstractGeneratorContext.log.info(String.format("\tFailed while processing entity (%s) - %s", entity.getName(), e.getMessage()));
					throw e;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
