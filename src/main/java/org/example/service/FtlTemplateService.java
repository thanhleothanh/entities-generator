package org.example.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.example.EntityGenerator;
import org.example.model.entity.Entity;

public class FtlTemplateService implements TemplateService {

	@Override
	public void process(List<Entity> entities) {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
		cfg.setLocale(Locale.US);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		cfg.setClassForTemplateLoading(EntityGenerator.class, "/template");

		for (Entity entity : entities) {
			Map<String, Object> input = new HashMap<>();
			input.put("packageName", "org.example.model");
			input.put("entity", entity);
			try {
				Template template = cfg.getTemplate("_entity.ftl");
				Writer consoleWriter = new OutputStreamWriter(System.out);
				template.process(input, consoleWriter);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
