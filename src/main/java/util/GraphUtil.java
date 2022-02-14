package util;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;

/**
 * 图，由节点和边构成
 * 1、 添加节点，提供节点名称， 缓存节点信息
 * 2、 添加边、边严格区分方法，src->dst ,如果需要双向， 需要两条边
 * 3、 如果是树，仅提取叶子节点构建依赖，忽略中间父级节点
 * 3.1、叶子节点的依赖继承父节点的依赖
 *
 * @author 54117
 * @date 2021年6月18日
 */
@Data
@Slf4j
public class GraphUtil<T> {
//    Logger log = Logger.getLogger(Graph.class.getName());
    /**
     * 记录边的信息
     */
    //    HashMap<String, Edge> taskEdgeMap = new HashMap<>();
    /**
     * 记录边的信息
     * src - dst  : edge
     */
    //    Table<String, String, Edge> taskEdgeTable = HashBasedTable.create();
    Map<String, Map<String, Edge>> taskEdgeTable = new HashMap<>();
    /**
     * 按序记录节点
     */
    LinkedHashMap<String, Node<T>> taskNodeMap = new LinkedHashMap<>();

    Comparator<Node<T>> comparator;
    /*    *//**
     * 节点名提取
     *//*
    Function<T, String> nodeNameProvider;
    *//**
     * 节点前置依赖提取， 单向关系，src-> dst ,此处为node 的src's nodeName
     *//*
    Function<T, List<String>> nodeSrcProvider;*/
    /**
     * 是否允许循环
     */
    boolean allowCirclar = false;

    public GraphUtil() {
    }

    //    public Map<String,Map< String, Edge>> getTaskEdgeTable() {
    //        return taskEdgeTable.rowMap();
    //    }

    //    public Graph(Function<T, String> nodeNameProvider, Function<T, List<String>> nodeSrcProvider) {
    //        Assert.notNull(nodeNameProvider);
    //        Assert.notNull(nodeSrcProvider);
    //
    //        this.nodeNameProvider = nodeNameProvider;
    //        this.nodeSrcProvider = nodeSrcProvider;
    //    }

    /**
     * 节点
     *
     * @param <T>
     */
    @Data
    @NoArgsConstructor
    public static class Node<T> {
        private String name;
        private T obj;
        private boolean visited = false;
        private boolean isVirtual = false;
        private int weight;
        private List<Node<T>> tails;

        public Node(String name, T obj) {
            this.name = name;
            this.obj = obj;
            tails = new ArrayList<>();// Collections.synchronizedList(new ArrayList<>());
        }

        public Node(String name, T obj, boolean isVirtual) {
            this.name = name;
            this.obj = obj;
            tails = new LinkedList<>();// Collections.synchronizedList(new ArrayList<>());
            this.isVirtual = isVirtual;
        }
    }

    /**
     * 边
     */
    @Data
    @NoArgsConstructor
    public static class Edge {

        private String srcName;
        private String dstName;
        private boolean isVirtual;

        public Edge(String srcName, String dstName) {
            this.srcName = srcName;
            this.dstName = dstName;
        }

        public Edge(String srcName, String dstName, boolean isVirtual) {
            this.srcName = srcName;
            this.dstName = dstName;
            this.isVirtual = isVirtual;
        }
    }

    /**
     * 添加节点， 必须在添加边前添加
     *
     * @param taskNode node
     */
    public void addNode(Node<T> taskNode) {
        // 已经存在的节点，不需要重复添加
        if (taskNodeMap.containsKey(taskNode.getName())) {
            log.info("node already existed! {}", taskNode.getName());
            return;
        }
        taskNodeMap.putIfAbsent(taskNode.getName(), taskNode);
    }

    /**
     * 添加边
     *
     * @param edge edge
     */
    public void addEdge(Edge edge) {
        Assert.notNull(edge.getSrcName());
        Assert.notNull(edge.getDstName());
        //        String key = edge.getSrcName() + ">" + edge.getDstName();
        taskEdgeTable.putIfAbsent(edge.getSrcName(), new HashMap<>());
        if (taskEdgeTable.get(edge.getSrcName()).containsKey(edge.getDstName())) {
            //            log.warn("edge already existed, skip ! {}->{}", edge.getSrcName(), edge.getDstName());
            return;
        }
        //log.debug("add edge {} -> {}", edge.getSrcName(), edge.getDstName());
        //        taskEdgeMap.putIfAbsent(key, edge);
        taskEdgeTable.get(edge.getSrcName()).put(edge.getDstName(), edge);
        Assert.notNull(taskNodeMap.get(edge.getSrcName()));
        Assert.notNull(taskNodeMap.get(edge.getDstName()));

        // 建立关系
        buildTails(edge);
    }

    public Node<T> calcWeight(Node<T> root) {
        root.setWeight(0);
        // init weight
        TreeUtil.topdown(root, Node::getTails, (parent, child) -> {
            if (null != parent) {
                int weight = parent.getWeight() + 1;
                child.setWeight(weight);
            }
        });
        return root;
    }

    public GraphUtil<T> sortByWeight(Node<T> node) {
        // 计算 weight , 每个节点找到被引用的节点,将其的权重相加
        TreeUtil.bottomup(null, node, Node::getTails, (parent, child) -> {
        }, (n1, n2) -> {
            Node node1 = (Node) n1;
            Node node2 = (Node) n2;
            return -Integer.compare(node1.getWeight(), node2.getWeight());
        });
        return this;
    }

    /**
     * 建立指向关系
     *
     * @param edge
     */
    public void buildTails(Edge edge) {
        Node<T> source = taskNodeMap.get(edge.getSrcName());
        Node<T> dest = taskNodeMap.get(edge.getDstName());
        source.getTails().add(dest);
        if (null != comparator) {
            source.getTails().sort(comparator);
        }
        // 逐个节点检查是否形成环，如果不允许环时给出提示
        if (!isAllowCirclar()) {
            List<String> checkCricleList = new ArrayList<>();
            checkCricleList.add(source.getName());
            TreeUtil.topdown(source, Node::getTails, (parent, tail) -> {
                checkCricleList.add(tail.getName());
                if (source.getName().equals(tail.getName())) {
                    throw new RuntimeException(String.format("节点%s存在循环:%s", source.getName(), String.join("->", checkCricleList)));
                }
            });
        }
    }

    public static <T> GraphUtil<T> create(List<T> list, Function<T, String> nodeNameProvider) {
        GraphUtil<T> graph = new GraphUtil<>();
        // add node
        list.forEach(n -> graph.addNode(new Node<T>(nodeNameProvider.apply(n), n)));
        return graph;
    }

    public GraphUtil<T> sort(Comparator<Node<T>> comparator) {
        this.comparator = comparator;
        return this;
    }

    public <E> GraphUtil<T> addEdge(List<E> edgeList, Function<E, String> src, Function<E, String> dst) {
        edgeList.forEach(e -> {
            String srcName = src.apply(e);
            String dstName = dst.apply(e);
            if (null != srcName && dstName != null) {
                this.addEdge(new Edge(srcName, dstName));
            }

        });
        return this;
    }

    public <E> GraphUtil<T> addEdge(String srcName, String dstName) {
        if (null != srcName && dstName != null) {
            this.addEdge(new Edge(srcName, dstName));
        }
        return this;
    }

    /**
     * 普通列表构建图，仅支持单向边
     *
     * @param list             列表
     * @param nodeNameProvider 节点名提供
     * @param nodeSrcProvider  节点前置依赖提供，单向
     * @param <T>              源数据对象
     * @return 图信息
     */
    public static <T> GraphUtil<T> buildGraph(List<T> list, Function<T, String> nodeNameProvider, Function<T, List<String>> nodeSrcProvider) {
        GraphUtil<T> graph = new GraphUtil<>();

        // add node
        list.forEach(n -> graph.addNode(new Node<T>(nodeNameProvider.apply(n), n)));

        // add edge
        list.forEach(n -> {
            List<String> srcList = nodeSrcProvider.apply(n);
            if (null != srcList) {
                srcList.forEach(src -> graph.addEdge(new Edge(src, nodeNameProvider.apply(n))));
            }
        });
        return graph;
    }

    /**
     * 构建树的图
     * 1、 只关注叶子节点间的依赖
     * 2、 叶子节点需要继承父节点的依赖
     *
     * @param trees            树
     * @param nodeNameProvider 节点名字提供方法
     * @param nodeSrcProvider  提供节点依赖的关系方法
     * @param <T>              继承TreeEntity的对象
     * @return 图信息
     */
    public static <T > GraphUtil<T> buildGraphByTree(List<T> trees,
        Function<T,List<T>> childrenGetter, Function<T,Boolean> leafGetter, Function<T, String> nodeNameProvider,
        Function<T, List<String>> nodeSrcProvider, Boolean allowCirclar) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("create graph");
        log.info("start to create graph! ");
        GraphUtil<T> graph = new GraphUtil<>();
        // 默认允许成环
        graph.setAllowCirclar(Optional.ofNullable(allowCirclar).orElse(false));
        // -------------------add node to graph------------------------------
        //依赖关系构建并存储到 map中，不修改原对象中的属性
        Map<String, List<String>> nameSrcMap = new HashMap<>();
        // 所有叶子节点任务
        List<T> leafList = new ArrayList<>();
        // map all node first
        Map<String, T> nodeMap = new HashMap<>();
        TreeUtil.topdown(trees, childrenGetter, (parent, child) -> {
            String childWbsCode = nodeNameProvider.apply(child);
            List<String> srcList = nodeSrcProvider.apply(child);
            if (null != srcList && srcList.size() > 0) {
                nameSrcMap.put(childWbsCode, srcList);
            } else if (null != parent && nameSrcMap.containsKey(nodeNameProvider.apply(parent))) {
                // 父节点依赖不为空， 子节点依赖为空、依赖继承
                nameSrcMap.put(childWbsCode, nameSrcMap.get(nodeNameProvider.apply(parent)));
            }
            graph.addNode(new Node<>(childWbsCode, child, !leafGetter.apply(child)));
            if (leafGetter.apply(child)) {
                leafList.add(child);
            }
            nodeMap.put(childWbsCode, child);
        });
        //非叶子节点构建虚拟边，但是也需要校验环路
        TreeUtil.topdown(trees, childrenGetter, (parent, child) -> {
            List<String> preDependenciesVirtual = nodeSrcProvider.apply(child);
            if (preDependenciesVirtual != null) {
                preDependenciesVirtual.forEach(next -> {
                    T t = nodeMap.get(next);
                    graph.addEdge(new Edge(next, nodeNameProvider.apply(child), !leafGetter.apply(t) || !leafGetter.apply(child)));
                });
            }
        });
        // 当前置任务非叶子节点时， cache该前置任务的最终不互相依赖的叶子节点
        Map<String, List<T>> cacheNoLeafNode = new HashMap<>();

        // -------------------add edge to graph------------------------------
        for (T t : leafList) {
            String nodeName = nodeNameProvider.apply(t);
            List<String> srcList = nameSrcMap.get(nodeName);
            if (srcList != null && srcList.size() > 0) {
                for (String preNodeName : srcList) {
                    T preNode = nodeMap.get(preNodeName);
                    if (null == preNode) {
                        continue;
                    }
                    // 如果前置任务非叶子节点
                    if (!leafGetter.apply(preNode)) {
                        List<T> preLeafChildren = Optional.ofNullable(cacheNoLeafNode.get(preNodeName)).orElse(new ArrayList<>());
                        boolean cached = cacheNoLeafNode.containsKey(preNodeName);
                        if (!cached) {
                            Set<String> currentSrcSet = new HashSet<>();
                            TreeUtil.topdown(preNode, childrenGetter, (p, c) -> {
                                if (leafGetter.apply(c)) {
                                    preLeafChildren.add(c);
                                    List<String> preDependenciesLeaf = nodeSrcProvider.apply(c);
                                    if (preDependenciesLeaf != null) {
                                        currentSrcSet.addAll(preDependenciesLeaf);
                                    }
                                } else {
                                    //中间节点的编码
                                    currentSrcSet.add(nodeNameProvider.apply(c));
                                }
                            });
                            // children 间如果有依赖，删除被依赖的节点，包括中间节点的依赖
                            preLeafChildren.removeIf(next -> currentSrcSet.contains(nodeNameProvider.apply(next)));
                            // cache the preLeafChildren
                            cacheNoLeafNode.put(preNodeName, preLeafChildren);
                        }
                        // 前置节点 -> 当前节点
                        preLeafChildren.forEach(c -> graph.addEdge(new Edge(nodeNameProvider.apply(c), nodeName)));
                    } else {
                        // 都是叶子节点 ，前置节点 -> 当前节点
                        Edge edge = new Edge(preNodeName, nodeName);
                        graph.addEdge(edge);
                    }
                }
            }
        }
        stopWatch.stop();
        log.info("data  node volume {},edge volume {}  build graph total cost :{}ms", graph.taskNodeMap.size(), graph.taskEdgeTable.size(),
            stopWatch.getTotalTimeMillis());
        return graph;
    }

}
