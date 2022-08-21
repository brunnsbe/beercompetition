package fi.homebrewing.competition.htmlcontroller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public abstract class HtmlAdminController {
    public HtmlAdminController() {
    }

    public abstract String getTemplateList();

    public abstract String getTemplateForm();

    public abstract String getActivePage();

    public abstract String getSingleModelName();

    protected abstract Map<String, ?> getFormModelAttributes();

    public void addModelAttributes(Model model, Map<String, ?> attributes) {
        model.addAllAttributes(attributes);
        model.addAttribute("activePage", getActivePage());
        model.addAttribute("showNavBar", true);
        model.addAttribute("showFilters", true);
    }

    protected String getRowsList(Model model, Map<String, ?> modelAttributes) {
        addModelAttributes(model, modelAttributes);
        return getTemplateList();
    }

    protected <T> String getRowForm(Optional<UUID> oId,
                                    JpaRepository<T, UUID> repository,
                                    Model model,
                                    Map<String, ?> modelAttributes,
                                    Supplier<T> newRow) {

        final T row = oId.map(id -> {
            return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));
        }).orElseGet(newRow);

        model.addAttribute(getSingleModelName(), row);
        addModelAttributes(model, modelAttributes);

        return getTemplateForm();
    }

    protected <T> String upsertRow(T row,
                                   BindingResult result,
                                   Model model,
                                   Supplier<Map<String, ?>> modelAttributes,
                                   JpaRepository<T, UUID> repository) {

        return upsertRow(row, result, model, modelAttributes, repository, () -> {});
    }

    protected <T> String upsertRow(T row,
                                   BindingResult result,
                                   Model model,
                                   Supplier<Map<String, ?>> modelAttributes,
                                   JpaRepository<T, UUID> repository,
                                   Runnable runAfterSave) {
        if (result.hasErrors()) {
            addModelAttributes(model, modelAttributes.get());
            return getTemplateForm();
        }

        repository.save(row);
        runAfterSave.run();

        return "redirect:" + getActivePage();
    }

    protected <T> String deleteRow(UUID id, JpaRepository<T, UUID> repository) {
        T row = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id:" + id));

        repository.delete(row);
        return "redirect:" + getActivePage();
    }
}
