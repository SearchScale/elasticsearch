/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the "Elastic License
 * 2.0", the "GNU Affero General Public License v3.0 only", and the "Server Side
 * Public License v 1"; you may not use this file except in compliance with, at
 * your election, the "Elastic License 2.0", the "GNU Affero General Public
 * License v3.0 only", or the "Server Side Public License, v 1".
 */


package org.elasticsearch.index.codec.vectors;

import org.apache.lucene.codecs.KnnVectorsFormat;
import org.apache.lucene.codecs.KnnVectorsReader;
import org.apache.lucene.codecs.KnnVectorsWriter;
import org.apache.lucene.index.SegmentReadState;
import org.apache.lucene.index.SegmentWriteState;
import org.apache.lucene.sandbox.vectorsearch.CuVSVectorsFormat;
import org.apache.lucene.sandbox.vectorsearch.CuVSVectorsWriter;

import java.io.IOException;

public class CuVSVectorFormat extends KnnVectorsFormat {
    public final int writerThreads;
    public final int intGraphDegree;
    public final int graphDegree;

    private CuVSVectorsFormat delegate;

    public CuVSVectorFormat() {
        super("cuvsvectorformat");
        this.writerThreads = 1;
        this.intGraphDegree = 128;
        this.graphDegree = 64;
        delegate = new CuVSVectorsFormat(this.writerThreads,
            this.intGraphDegree,
            this.graphDegree,
            CuVSVectorsWriter.MergeStrategy.NON_TRIVIAL_MERGE);
    }

    public CuVSVectorFormat(int writerThreads, int intGraphDegree, int graphDegree) {
        super("cuvsvectorformat");
        this.writerThreads = writerThreads;
        this.intGraphDegree = intGraphDegree;
        this.graphDegree = graphDegree;
        delegate = new CuVSVectorsFormat(writerThreads,
            intGraphDegree,
            graphDegree,
            CuVSVectorsWriter.MergeStrategy.NON_TRIVIAL_MERGE);
    }

    @Override
    public KnnVectorsWriter fieldsWriter(SegmentWriteState state) throws IOException {
        return delegate.fieldsWriter(state);
    }

    @Override
    public KnnVectorsReader fieldsReader(SegmentReadState state) throws IOException {
        return delegate.fieldsReader(state);
    }

    @Override
    public int getMaxDimensions(String fieldName) {
        return delegate.getMaxDimensions(fieldName);
    }

    public String toString() {
        return "CuVSVectorFormat(name=CuVSVectorFormat, writerThreads="
            + writerThreads
            + ", intGraphDegree="
            + intGraphDegree
            + ", graphDegree="
            + graphDegree
            + ")";
    }
}
