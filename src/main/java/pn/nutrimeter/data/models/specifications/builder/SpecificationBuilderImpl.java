package pn.nutrimeter.data.models.specifications.builder;

import org.springframework.data.jpa.domain.Specification;
import pn.nutrimeter.data.models.specifications.SearchCriteria;
import pn.nutrimeter.data.models.specifications.builder.SpecificationBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpecificationBuilderImpl<T> implements SpecificationBuilder<T> {

    private List<SearchCriteria> params;

    public SpecificationBuilderImpl() {
        this.params = new ArrayList<>();
    }

    @Override
    public SpecificationBuilder<T> with(SearchCriteria criteria) {
        this.params.add(criteria);
        return this;
    }

    @Override
    public Specification<T> build(Function<SearchCriteria, Specification<T>> mappingToSpecification) {
        if (this.params.size() == 0) {
            return null;
        }

        List<Specification<T>> specs = this.params
                .stream()
                .map(mappingToSpecification)
                .collect(Collectors.toList());

        Specification<T> result = specs.get(0);

        for (int i = 1; i < specs.size(); i++) {
            result = Objects.requireNonNull(Specification.where(result)).and(specs.get(i));
        }

        return result;
    }
}
