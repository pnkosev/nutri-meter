package pn.nutrimeter.data.models.specifications;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;

public class TableJoiner {
    public static <E1, E2> Specification<E1> join(JoinCriteria joinCriteria) {
        return (Specification<E1>) (root, criteriaQuery, criteriaBuilder) -> {
            Join<E1, E2> join = root.join(joinCriteria.getTable());
            return criteriaBuilder.equal(join.get(joinCriteria.getField()), joinCriteria.getValue());
        };
    }
}
