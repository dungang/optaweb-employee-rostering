/*
 * Copyright (C) 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.demo;

import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.gwt.event.dom.client.ClickEvent;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.MouseEvent;
import jsinterop.base.Js;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.list.ListElementView;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.list.ListView;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.model.LinearScale;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.model.Viewport;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.powers.Draggability;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.powers.Resizability;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.view.BlobView;
import org.optaplanner.openshift.employeerostering.gwtui.client.beta.java.view.SubLaneView;

@Templated
public class ShiftBlobView implements BlobView<ShiftBlob> {

    @Inject
    @DataField("blob")
    private HTMLDivElement root;

    @Inject
    @Named("span")
    @DataField("label")
    private HTMLElement label;

    @Inject
    private Draggability draggability;

    @Inject
    private Resizability resizability;

    private ShiftBlob blob;
    private Viewport viewport;
    private SubLaneView subLaneView;

    private ListView<ShiftBlob> list;

    @Override
    public ListElementView<ShiftBlob> setup(final ShiftBlob blob,
                                            final ListView<ShiftBlob> list) {

        final LinearScale<LocalDateTime> scale = viewport.domainScaleInGridPixels;

        this.blob = blob;
        this.list = list;

        updateLabel();
//        viewport.setSizeInScreenPixels(this, scale.from(blob.getSize()), 0);
//        viewport.setPositionInScreenPixels(this, scale.to(blob.getPosition()), 0);

        draggability.onDrag(this::onDrag);
        draggability.applyFor(this, subLaneView, viewport, blob);

        resizability.onResize(this::onResize);
        resizability.applyFor(this, subLaneView, viewport, blob);

        return this;
    }

    private boolean onResize(final int newSize) {
        final LinearScale<LocalDateTime> scale = viewport.domainScaleInGridPixels;
//        blob.setSize(scale.to(newSize));
        updateLabel();
        return true;
    }

    private boolean onDrag(final int newPosition) {
        final LinearScale<LocalDateTime> scale = viewport.domainScaleInGridPixels;
//        blob.setPosition(scale.to(newPosition));
        updateLabel();
        return true;
    }

    @EventHandler("blob")
    public void onBlobClicked(final ClickEvent event) {
        final MouseEvent e = Js.cast(event.getNativeEvent());

        if (e.altKey) {
            list.remove(blob);
        }
    }

    private void updateLabel() {
        label.textContent = blob.getLabel().charAt(0) + " [" + blob.getPosition() + ", " + blob.getSize() + "]";
    }

    @Override
    public BlobView<ShiftBlob> withViewport(final Viewport viewport) {
        this.viewport = viewport;
        return this;
    }

    @Override
    public BlobView<ShiftBlob> withSubLaneView(final SubLaneView subLaneView) {
        this.subLaneView = subLaneView;
        return this;
    }
}