def call(Closure body) {
    podTemplate(
        label: 'default',
        containers: [
            containerTemplate(name: 'linux-dev', image: "localhost:5000/server/linux-dev:0.1", alwaysPullImage: true, resourceRequestMemory: '4Gi', resourceLimitMemory: '4Gi', resourceRequestCpu: '500m'),
        ])
    {
        body()
    }
}
