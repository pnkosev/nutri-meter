package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Tag;
import pn.nutrimeter.data.repositories.TagRepository;
import pn.nutrimeter.service.models.TagServiceModel;
import pn.nutrimeter.service.services.api.TagService;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    private final ModelMapper modelMapper;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper modelMapper) {
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void create(TagServiceModel tagServiceModel) {
        Tag tag = this.modelMapper.map(tagServiceModel, Tag.class);
        this.tagRepository.saveAndFlush(tag);
    }
}
