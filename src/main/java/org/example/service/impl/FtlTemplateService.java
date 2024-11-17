package org.example.service.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.File;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.example.AbstractGeneratorContext;
import org.example.model.entity.Entity;
import org.example.service.TemplateService;

public class FtlTemplateService implements TemplateService {

	@Override
	public void process(List<Entity> entities, File outputDirectory) {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
		cfg.setLocale(Locale.US);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setClassForTemplateLoading(AbstractGeneratorContext.class, "/template");

		try {
			Template template = cfg.getTemplate("_entity.ftl");
			for (Entity entity : entities) {
				Map<String, Object> config = new HashMap<>();
				config.put("packageName", AbstractGeneratorContext.packageName);
				config.put("entity", entity);
				try (Writer fileWriter = Files.newBufferedWriter(Path.of(outputDirectory.getPath(), String.format("%s.java", entity.getName())))) {
					template.process(config, fileWriter);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
