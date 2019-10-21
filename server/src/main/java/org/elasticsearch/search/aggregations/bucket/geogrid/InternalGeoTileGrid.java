/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.search.aggregations.bucket.geogrid;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Represents a grid of cells where each cell's location is determined by a geohash.
 * All geohashes in a grid are of the same precision and held internally as a single long
 * for efficiency's sake.
 */
public class InternalGeoTileGrid extends InternalGeoGrid<InternalGeoTileGridBucket> {

    InternalGeoTileGrid(String name, int requiredSize, long minDocCount, List<InternalGeoGridBucket> buckets,
                        List<PipelineAggregator> pipelineAggregators, Map<String, Object> metaData) {
        super(name, requiredSize, minDocCount, buckets, pipelineAggregators, metaData);
    }

    public InternalGeoTileGrid(StreamInput in) throws IOException {
        super(in);
    }

    @Override
    public InternalGeoGrid create(List<InternalGeoGridBucket> buckets) {
        return new InternalGeoTileGrid(name, requiredSize, minDocCount, buckets, pipelineAggregators(), metaData);
    }

    @Override
    public InternalGeoGridBucket createBucket(InternalAggregations aggregations, InternalGeoGridBucket prototype) {
        return new InternalGeoTileGridBucket(prototype.hashAsLong, prototype.docCount, aggregations);
    }

    @Override
    InternalGeoGrid create(String name, int requiredSize, long minDocCount, List buckets, List list, Map metaData) {
        return new InternalGeoTileGrid(name, requiredSize, minDocCount, buckets, list, metaData);
    }

    @Override
    Reader getBucketReader() {
        return InternalGeoTileGridBucket::new;
    }

    @Override
    public String getWriteableName() {
        return GeoTileGridAggregationBuilder.NAME;
    }
}
