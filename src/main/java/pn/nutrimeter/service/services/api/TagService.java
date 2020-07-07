package pn.nutrimeter.service.services.api;

import org.springframework.beans.factory.annotation.Autowired;
import pn.nutrimeter.service.models.TagServiceModel;

public interface TagService {

    void create(TagServiceModel tagServiceModel);
}
