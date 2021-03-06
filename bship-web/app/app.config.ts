module App {

    let app = angular.module(Module);

    export class Config {

        static id: string = "config";

        protected name: string = "bship";
        protected version: string = "1.0";

        constructor() {

        }
    }

    app.constant(Config.id, new Config());

    app.config(($logProvider: ng.ILogProvider) => {

        if ($logProvider.debugEnabled) {

            $logProvider.debugEnabled(true);
        }
    });
}