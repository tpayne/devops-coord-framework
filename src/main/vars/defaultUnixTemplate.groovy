def call(Closure body) {
    podTemplate(
        label: 'default',
        containers: [
            containerTemplate(name: 'linux-std', image: "linux-std", alwaysPullImage: true, resourceRequestMemory: '4Gi', resourceLimitMemory: '4Gi', resourceRequestCpu: '500m'),
        ])
    {
        body()
    }
}
