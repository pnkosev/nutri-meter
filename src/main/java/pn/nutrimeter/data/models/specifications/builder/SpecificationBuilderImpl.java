package pn.nutrimeter.data.models.specifications.builder;

import org.springframework.data.jpa.domain.Specification;
import pn.nutrimeter.data.models.specifications.JoinCriteria;
import pn.nutrimeter.data.models.specifications.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.criteria.Join;

public class SpecificationBuilderImpl<T> implements SpecificationBuilder<T> {

    private List<SearchCriteria> params;

    private List<Specification<T>> joins;

    public SpecificationBuilderImpl() {
        this.params = new ArrayList<>();
        this.joins = new ArrayList<>();
    }

    @Override
    public SpecificationBuilder<T> with(SearchCriteria criteria) {
        this.params.add(criteria);
        return this;
    }

    @Override
    public <E> SpecificationBuilder<T> join(JoinCriteria joinCriteria) {
        Specification<T> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Join<T, E> join = root.join(joinCriteria.getTable());
            return criteriaBuilder.equal(join.get(joinCriteria.getField()), joinCriteria.getValue());
        };
        this.joins.add(specification);
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

        for (Specification<T> join : this.joins) {
            result = Objects.requireNonNull(Specification.where(result)).and(join);
        }

        return result;
    }
}
