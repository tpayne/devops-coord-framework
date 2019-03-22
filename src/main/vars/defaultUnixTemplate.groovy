def call(Closure body) {
    podTemplate(
        label: 'default',
        containers: [
            containerTemplate(name: 'linux-base', image: "ubuntu", alwaysPullImage: true, resourceRequestMemory: '4Gi', resourceLimitMemory: '4Gi', resourceRequestCpu: '500m'),
        ])
    {
        body()
    }
}
