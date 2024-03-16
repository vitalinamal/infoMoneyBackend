package prog.academy.infomoney.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import prog.academy.infomoney.dto.request.CategoryRequest;
import prog.academy.infomoney.dto.response.CategoryResponse;
import prog.academy.infomoney.entity.Category;
import prog.academy.infomoney.entity.Profile;
import prog.academy.infomoney.exceptions.ApplicationException;
import prog.academy.infomoney.repository.CategoryRepository;
import prog.academy.infomoney.repository.TransactionRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    private final TransactionRepository transactionRepository;
    private final ProfileService profileService;

    @Transactional
    public void createCategory(CategoryRequest request) {
        Profile profile = profileService.getProfileById(request.profileId());

        profile.getCategories().stream().filter(category -> category.getName().equals(request.name()))
                .findAny().ifPresent(category -> {
                    throw new ApplicationException(STR."Category with this name (\{category.getName()}) already exists on this profile: \{profile.getName()}");
                });

        Category category = Category.builder()
                .name(request.name())
                .description(request.description())
                .profile(profile)
                .build();
        repository.save(category);
    }


    public List<CategoryResponse> findAllCategoriesByProfile(Long id) {
        return repository.findAllByProfileId(id).stream().map(this::mapToCategoryResponse).collect(toList());
    }

    public CategoryResponse findCategoryById(Long id) {
        var category = getCategoryById(id);
        return mapToCategoryResponse(category);
    }

    @Transactional
    public void updateCategory(Long id, CategoryRequest request) {
        if (repository.existsByProfileIdAndNameAndNotId(request.name(), request.profileId(), id)) {
            throw new ApplicationException(STR."Profile already have a category with this name:  \{request.name()}");
        }
        var category = getCategoryById(id);
        category.setName(request.name());
        category.setDescription(request.description());
        repository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        removeCategoryFromTransactions(id, category);
        repository.delete(category);
    }

    private void removeCategoryFromTransactions(Long id, Category category) {
        var transactions = transactionRepository.findAllByProfileAndCategory(category.getProfile(), id);
        transactions.forEach(t -> t.setCategory(null));
        transactionRepository.saveAll(transactions);
    }

    public Category getCategoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApplicationException(STR."Category with id \{id} does not exist"));
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
