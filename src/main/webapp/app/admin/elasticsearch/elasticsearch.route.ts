import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Route, CanActivate } from '@angular/router';
import { ElasticsearchReindexDialogComponent } from './elasticsearch-reindex-dialog.component';

export const elasticSearchRoute: Route = {
    path: 'elasticsearch/reindex',
    component: ElasticsearchReindexDialogComponent,
    data: {
        authorities: ['ROLE_ADMIN'],
        pageTitle: 'Reindex Elastic Search'
    }
};
