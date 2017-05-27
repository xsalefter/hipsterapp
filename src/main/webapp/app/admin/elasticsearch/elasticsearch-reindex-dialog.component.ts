import { Component } from '@angular/core';
import { ElasticSearchService } from './elasticsearch.service';

@Component({
    templateUrl: './elasticsearch-reindex-dialog.component.html'
})
export class ElasticsearchReindexDialogComponent {

    constructor(private service: ElasticSearchService) {
    }

    reIndex() {
        this.service.reIndex();
    }
}
