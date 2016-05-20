package com.datazuul.iiif.bookshelf.backend.repository;

import com.datazuul.iiif.bookshelf.model.IiifManifestSummary;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository for IiifManifestSummaries identified by the manifest URI.
 */
public interface IiifManifestSummaryRepository extends MongoRepository<IiifManifestSummary, UUID>, IiifManifestSummaryRepositoryCustom {

  public IiifManifestSummary findByManifestUri(String manifestUri);

  public List<IiifManifestSummary> findAllByOrderByLastModifiedDesc();

  public Page<IiifManifestSummary> findAllByOrderByLastModifiedDesc(Pageable pageRequest);

  public Page<IiifManifestSummary> findBy(TextCriteria criteria, Pageable page); // do not expose mongo TextCriteria to service layer!

  public Page<IiifManifestSummary> findByUuidIn(List<UUID> uuids, Pageable page);

  public List<IiifManifestSummary> findByUuidIn(List<UUID> uuids);
}