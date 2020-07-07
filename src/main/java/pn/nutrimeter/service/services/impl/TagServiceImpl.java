package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.Tag;
import pn.nutrimeter.data.repositories.TagRepository;
import pn.nutrimeter.service.models.TagServiceModel;
import pn.nutrimeter.service.services.api.TagService;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<TagServiceModel> getAll() {
        return this.tagRepository
                .findAll()
                .stream()
                .map(t -> this.modelMapper.map(t, TagServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTag(String tagId) {
        this.tagRepository.delete(this.tagRepository.findById(tagId).get());
    }
}
