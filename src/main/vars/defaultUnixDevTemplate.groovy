def call(Closure body) {
    podTemplate(
        label: 'default',
        containers: [
            containerTemplate(name: 'linux-dev', image: "rhub/ubuntu-gcc-release", alwaysPullImage: true, resourceRequestMemory: '4Gi', resourceLimitMemory: '4Gi', resourceRequestCpu: '500m'),
        ])
    {
        body()
    }
}
