## 基于k8s构建微服务

- k8s构建微服务与springcloud的差异
  - 服务发现: k8s自带服务发现机制[service], 取代[nacos], 需要添加依赖spring-cloud-starter-kubernetes
  - 网关服务: k8s有网关服务插件[ingress], 取代[gateway]
  - 日志收集: 采用[EFK]取代[ELK]
  - 授权与认证?
  
- 开发环境
  - 操作系统: Window10 1909
  - 开发工具: Idea 2020.1
  - java环境: JDK11
  - k8s环境: minikube 1.12.3
  - 持续集成: CircleCI(对Github中开源的项目免费, 对私有项目收费)
  - 持续交付: ArgoCD

- Terminal使用说明
    - idea打开时需要以管理员身份运行, 这样才能运行powershell
    - 在settings -> tools -> terminal -> shell path 设置为 powershell.exe
    - 打开terminal后, 运行docker-env.ps1, 这样可以使用minikube环境的docker, 或者使用以下方式
    - 如果要镜像构建到minikube的docker环境, 执行 `& minikube -p minikube docker-env | Invoke-Expression`, 使用`minikube docker-env`可查看不同系统切换环境的方法